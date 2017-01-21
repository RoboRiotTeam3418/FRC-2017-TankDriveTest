package com.team3418.frc2016.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.team3418.frc2016.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	
	static Shooter mInstance = new Shooter();

    public static Shooter getInstance() {
        return mInstance;
    }    
    
    private CANTalon mShooterTalon = new CANTalon(0);
	
    
    public Shooter() {
    	//initialize shooter hardware settings
		System.out.println("Shooter Initialized");
		//leftshooter
				
		mShooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		mShooterTalon.enableBrakeMode(false);
		mShooterTalon.reverseSensor(false);
		mShooterTalon.reverseOutput(true);
		mShooterTalon.changeControlMode(TalonControlMode.Speed);
		mShooterTalon.set(0);
		mShooterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mShooterTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		mShooterTalon.setProfile(0);
		mShooterTalon.setPID(Constants.kFlywheelKp, Constants.kFlywheelKi, Constants.kFlywheelKd, Constants.kFlywheelKf,
                Constants.kFlywheelIZone, Constants.kFlywheelRampRate, 0);
		
		mTargetRpm = 0;
		mTargetSpeed = 0;
	}
    
    public enum ShooterState {
    	STOP, SET_RPM, SET_OPEN_LOOP
    }
    
    public enum ShooterReadyState {
    	READY, NOT_READY
    }
    

    
    //
    private double mTargetSpeed;
    private double mTargetRpm;
    
    public void setTargetSpeed(double speed){
    	mTargetSpeed = speed*1000;
    }
    
    public void setTargetRpm(double rpm){
    	mTargetRpm = rpm;
    }
    
    public double getTargetRpm(){
    	return mTargetRpm;
    }
    
    public double getTargetSpeed(){
    	return mTargetSpeed;
    }
    //
    
    //
    private ShooterState mShooterState;
    private ShooterReadyState mShooterReadyState;
    
    public ShooterState getShooterState(){
    	return mShooterState;
    }
    
    public ShooterReadyState getShooterReadyState(){
    	return mShooterReadyState;
    }
    //
    
    
    
	@Override
	public void updateSubsystemState() {
		
		switch(mShooterState) {
		case SET_OPEN_LOOP:
			setOpenLoop(getTargetSpeed());
			break;
		case SET_RPM:
			setRpm(getTargetRpm()/*Constants.kFlywheelRpmSetpoint*/);
			break;
		case STOP:
			setOpenLoop(0);
			break;
		}
		
		printShooterInfo();
		outputToSmartDashboard();
		
		if (isOnTarget()){
			mShooterReadyState = ShooterReadyState.READY;
		} else {
			mShooterReadyState = ShooterReadyState.NOT_READY;
		}
	}
	
	
	//set state methods
	public void OpenLoopShooterState(){
		mShooterState = ShooterState.SET_OPEN_LOOP;
	}
	
	public void RpmShooterState(){
		mShooterState = ShooterState.SET_RPM;
	}
	
	public void stopShooterState(){
		mShooterState = ShooterState.STOP;
	}
	//
	
	
	
	//print shooter info to console
	private void printShooterInfo(){
		System.out.println(" out: "+getMotorOutput()+
				   " spd: "+mShooterTalon.getSpeed()+
		           " err: "+mShooterTalon.getClosedLoopError()+
		           " trgrpm: "+getTargetRpm()+
		           " trgspd: "+getTargetSpeed());
	}
	//
	
	
	//get shooter speed info methods
	private double getRpm(){
		return mShooterTalon.getSpeed();
	}
	
	private double getSetpoint(){
		return mShooterTalon.getSetpoint();
	}
	
	private double getMotorOutput(){
    	return mShooterTalon.getOutputVoltage() / mShooterTalon.getBusVoltage();
    }
	//
	
	//set shooter speed methods
	private void setRpm(double rpm){
		mShooterTalon.changeControlMode(TalonControlMode.Speed);
		mShooterTalon.set(rpm);
	}
	
	private void setOpenLoop(double speed){
		mShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
	}
	//
	
	//set shooter ready state
	private boolean isOnTarget(){
		return (mShooterTalon.getControlMode() == CANTalon.TalonControlMode.Speed
                && Math.abs(getRpm() - getSetpoint()) < Constants.kFlywheelOnTargetTolerance);
	}
	//
	
	

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("flywheel_rpm", getRpm());
        SmartDashboard.putNumber("flywheel_setpoint", mShooterTalon.getSetpoint());
        SmartDashboard.putBoolean("flywheel_on_target", isOnTarget());
        SmartDashboard.putNumber("flywheel_master_current", mShooterTalon.getOutputCurrent());
        SmartDashboard.putNumber("flywheel_target_rpm", getTargetRpm());
        SmartDashboard.putNumber("flywheel_target_speed", getTargetSpeed());
        SmartDashboard.putNumber("Motor_Output", getMotorOutput());
		SmartDashboard.putNumber("flywheel_Speed", mShooterTalon.getSpeed());
        SmartDashboard.putNumber("Closed_Loop_error", mShooterTalon.getClosedLoopError());
	}
    
    
    
    
}
