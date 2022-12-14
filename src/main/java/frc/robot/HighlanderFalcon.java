// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.controller.PIDController;

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

    /**Makes a new HighlanderFalcon with a PID controller built in */
    public HighlanderFalcon(int id, PIDController pid) {
        super(id);
        this.config_kP(0, pid.getP());
        this.config_kI(0, pid.getI());
        this.config_kD(0, pid.getD());
    }

    public void defaultConfigs() {
        this.enableVoltageCompensation(true);
        this.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 80, 0.5));
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

    /**Gets the encoder position in degrees */
    public double getDegrees() {
        return getRotations() * 360;
    }

    /**Gets the encoder position in radians */
    public double getRadians() {
        return getRotations() * Math.PI * 2;
    }

    /**Gets the encoder RPM */
    public double getRPM() {
        return super.getSelectedSensorVelocity() * 10 * 60;
    }

    /**Gets the encoder rotations per second */
    public double getRPS() {
        return getRPM() / 10;
    }

    /**Sets the onboard velocity PID */
    public void setTargetRPM(double rpm) {
        set(TalonFXControlMode.Velocity, rpm * ROTATIONS_TO_TICKS * 60 * 10);
    }

    /**Sets the onboard position PID, in rotations */
    public void setTargetRot(double rotations) {
        set(TalonFXControlMode.Position, rotations * ROTATIONS_TO_TICKS);
    }

    /**Sets the onboard position PID, in degrees */
    public void setTargetDegrees(double degrees) {
        setTargetRot(degrees / 360);
    }

    /**Sets the onboard position PID, in radians */
    public void setTargetRadians(double radians) {
        setTargetRot(radians / (Math.PI * 2));
    }

    /**Sets the output of the motor from -1 to 1*/
    public void setPercentOut(double percent) {
        set(TalonFXControlMode.PercentOutput, percent);
    }
}
