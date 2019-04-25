package com.skoneczny.wrapper;


public class TimeInLongValue {


	long h;
	long m;
	long s;
	
	public TimeInLongValue() {}
		
	public TimeInLongValue(long h, long m, long s) {		
		this.h = h;
		this.m = m;
		this.s = s;
	}
	
	
	public TimeInLongValue(long h, long m) {
		this.h = h;
		this.m = m;
	}


	public long getH() {
		return h;
	}
	public void setH(long h) {
		this.h = h;
	}
	public long getM() {
		return m;
	}
	public void setM(long m) {
		this.m = m;
	}
	public long getS() {
		return s;
	}
	public void setS(long s) {
		this.s = s;
	}

	

	@Override
	public String toString() {
		return "TimeInLongValue [h=" + h + ", m=" + m + ", s=" + s + "]";
	}
	
	public String toStingHoursMinutes() {	
		String str = "Przepracowano: " + h +  " godzin i " + m + " minut";	
		return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (h ^ (h >>> 32));
		result = prime * result + (int) (m ^ (m >>> 32));
		result = prime * result + (int) (s ^ (s >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeInLongValue other = (TimeInLongValue) obj;
		if (h != other.h)
			return false;
		if (m != other.m)
			return false;
		if (s != other.s)
			return false;
		return true;
	}
	
	
	
	
}
