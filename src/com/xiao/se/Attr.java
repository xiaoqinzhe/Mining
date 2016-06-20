package com.xiao.se;

public class Attr<T> {
	public String name;
	public T value;
	
	public Attr(String name,T value){
		this.name=name;
		this.value=value;
	}

	public boolean equals(Object anObject) {
		if (this == anObject) {
            return true;
        }
        if (anObject instanceof Attr<?>) {
            Attr another = (Attr)anObject;
            if (name == another.name&&value==another.value) {
                return true;
            }
        }
        return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Attr [name=" + name + ", value=" + value + "]";
	}
	
	
}
