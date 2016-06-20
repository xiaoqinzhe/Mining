package com.xiao.se;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class LinearLib {
	Model model;
	
	public void train(String file) throws IOException, InvalidInputDataException{
		Problem p=Problem.readFromFile(new File(file),0.05);
		model=Linear.train(p, new Parameter(SolverType.L2R_L1LOSS_SVC_DUAL,9,0.00000001));
		//model.save(new File("linearModel.model"));
		System.out.println(model.toString());
	}
	
	public double predict(List<Double> l){
		Feature[] fs=new FeatureNode[l.size()];
		for(int i=0;i<l.size();++i){
			fs[i]=new FeatureNode(i+1, l.get(i));
		}
		double res=Linear.predict(model, fs);
		//System.out.println(res);
		return res;
	}
	
}
