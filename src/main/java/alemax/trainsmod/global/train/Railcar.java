package alemax.trainsmod.global.train;

public abstract class Railcar {

    public static final int SYNC_TIME = 20;
    public static final int SPEED_SYNC_TIME = 3;

    public long railcarUniqueID;
    public Train train;

    int syncCooldown;
    int speedSyncCooldown;

    private double frontPosX;
    private double frontPosY;
    private double frontPosZ;

    private double rearPosX;
    private double rearPosY;
    private double rearPosZ;

    public float trainRotYaw;
    public float trainRotPitch;
    public float trainRotRoll; //for future stuff maybe

    private double speed;
    private double aimedSpeed;

    protected double maxSpeed;
    protected double maxAcceleration;
    protected double maxDeceleration;

    protected double axleDistance;
    protected int maxPassengersCount;
    protected Seat[] seats;

    public Railcar() {
        syncCooldown = SYNC_TIME;
        speedSyncCooldown = SPEED_SYNC_TIME;
        axleDistance = 6;
        this.maxSpeed = 30;
        this.aimedSpeed = 0;
        this.maxAcceleration = 1.5;
        this.maxDeceleration = 1.5;
        //this.trainUniqueID = TrainUID.generateID(); TODO
    }

}
