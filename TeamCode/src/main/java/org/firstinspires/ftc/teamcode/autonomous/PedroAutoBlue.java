package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.util.TelemetryData;

import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.LaunchBallsCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem;

@Autonomous(name = "Pedro Auto")
public class PedroAutoBlue extends CommandOpMode {
    private Follower follower;

    private Motor launcherMotor;
    private Motor loadMotor;

    private MotorEx intakeMotor;

    private LauncherSubsystem launcherSubsystem;

    private IntakeSubsystem intakeSubsystem;
    TelemetryData telemetryData = new TelemetryData(telemetry);

    // Poses
    private final Pose startPose = new Pose(8, 8, Math.toRadians(90));
    private final Pose scorePose = new Pose(47, 94, Math.toRadians(135));

    private final Pose prePickup1Pose = new Pose(40, 34, Math.toRadians(180));
    private final Pose pickup1Pose = new Pose(13, 34, Math.toRadians(180));

    private final Pose prePickup2Pose = new Pose(40, 57, Math.toRadians(180));
    private final Pose pickup2Pose = new Pose(13, 57, Math.toRadians(180));

    private final Pose prePickup3Pose = new Pose(40, 81, Math.toRadians(180));
    private final Pose pickup3Pose = new Pose(13, 81, Math.toRadians(180));
    private final Pose parkPose = new Pose(68, 96, Math.toRadians(90));

    // Path chains
    private PathChain initialShoot, grabPickup1, grabPickup2, grabPickup3, park;

    public void buildPaths() {
        initialShoot = follower.pathBuilder()
                .addPath(new BezierCurve(startPose, new Pose(64, 16) , scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prePickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .addPath(new BezierLine(prePickup1Pose, pickup1Pose))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(pickup1Pose, new Pose(64, 48), scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup2 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prePickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
                .addPath(new BezierLine(prePickup2Pose, pickup2Pose))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(pickup2Pose, new Pose(64, 48), scorePose))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        grabPickup3 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, prePickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
                .addPath(new BezierLine(prePickup3Pose, pickup3Pose))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(pickup3Pose, new Pose(64, 48), scorePose))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, parkPose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();
    }


    @Override
    public void initialize() {
        super.reset();

        launcherMotor = new Motor(hardwareMap, "launcherMotor");
        loadMotor = new Motor(hardwareMap, "launcherLoader");

        intakeMotor = new MotorEx(hardwareMap, "intakeMotor");

        launcherSubsystem = new LauncherSubsystem(launcherMotor, loadMotor, telemetry);

        intakeSubsystem = new IntakeSubsystem(intakeMotor, telemetry);

        // Initialize follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        buildPaths();


        // Schedule the autonomous sequence
        schedule(
                // Score preload
                new IntakeCommand(intakeSubsystem, -1.0, null),

                new FollowPathCommand(follower, initialShoot),
                new LaunchBallsCommand(launcherSubsystem, 1.0, 3, 2.0),

                new FollowPathCommand(follower, grabPickup1).setGlobalMaxPower(0.5),
                new LaunchBallsCommand(launcherSubsystem, 1.0, 3, 2.0),

                // Second pickup cycle
                new FollowPathCommand(follower, grabPickup2),
                new LaunchBallsCommand(launcherSubsystem, 1.0, 3, 2.0),

                // Third pickup cycle
                new FollowPathCommand(follower, grabPickup3),
                new LaunchBallsCommand(launcherSubsystem, 1.0, 3, 2.0),

                // Park
                new FollowPathCommand(follower, park)
        );
    }

    @Override
    public void run() {
        super.run();
        follower.update();

        telemetryData.addData("X", follower.getPose().getX());
        telemetryData.addData("Y", follower.getPose().getY());
        telemetryData.addData("Heading", follower.getPose().getHeading());
        telemetryData.update();
    }
}