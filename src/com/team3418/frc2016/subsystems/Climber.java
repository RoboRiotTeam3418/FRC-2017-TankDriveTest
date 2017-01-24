package com.team3418.frc2016.subsystems;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem {

	
	static Climber mInstance = new Climber();

    public static Climber getInstance() {
        return mInstance;
    }
    
    private Victor mClimberVictor = new Victor(2);
    
    
	@Override
	public void updateSubsystemState() {
		outputToSmartDashboard();
	}
	
	public void setSpeed(double speed){
		mClimberVictor.set(speed);
	}
	
	public void stop(){
		setSpeed(0);
	}
	//
	
	

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Climber_Power_Percent", mClimberVictor.getSpeed());
	}
    
    
    
    
}
