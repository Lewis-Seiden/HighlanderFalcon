// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

/** A talonfx wrapper. Lazy talon functionality is taken from 254 */
public class HighlanderFalcon extends TalonFX {
    private double lastSet = Double.NaN;
    private TalonFXControlMode lastControlMode = null;

    static double TICKS_TO_ROTATIONS = 1 / 2048;
    static double ROTATIONS_TO_TICKS = 2048;

    public HighlanderFalcon(int id) {
        super(id);
    }

    public HighlanderFalcon(int id, String canbus) {
        super(id, canbus);
    }

     /**Gets the last value sent to the motor */
     public double getLastSet() {
        return lastSet;
    }

    /**Sets the motors behaviour if the new value is different then the last one. */
    @Override
    public void set(TalonFXControlMode mode, double value) {
        if (value != lastSet || mode != lastControlMode) {
            lastSet = value;
            lastControlMode = mode;
            super.set(mode, value);
        }
    }

    /**Gets the encoder position in rotations */
    public double getRotations() {
        return super.getSelectedSensorPosition() * TICKS_TO_ROTATIONS;
    }

    public double getDegrees() {
        return getRotations() * 360;
    }

    public double getRadians() {
        return getRotations() * Math.PI * 2;
    }

    public double getRPM() {
        return super.getSelectedSensorVelocity() * 10 * 60;
    }

    public double getRPS() {
        return getRPM() / 10;
    }

    public void setRPM(double rpm) {
        set(TalonFXControlMode.Velocity, rpm * ROTATIONS_TO_TICKS * 60 * 10);
    }
}
