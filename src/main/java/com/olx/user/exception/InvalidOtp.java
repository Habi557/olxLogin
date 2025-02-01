package com.olx.user.exception;

public class InvalidOtp extends RuntimeException{
	private int otp;
	public InvalidOtp( int otp) {
		super();
		this.otp = otp;
	}
	@Override
	public String toString() {
		return "InvalidOtp [ otp=" + otp + "]";
	}
	

}
