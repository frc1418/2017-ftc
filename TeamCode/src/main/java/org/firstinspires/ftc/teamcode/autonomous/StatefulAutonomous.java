package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.teamcode.MecanumOpMode;
import org.firstinspires.ftc.teamcode.components.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fendleyc on 11/19/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@interface State{
    double duration() default 0.0;
    boolean timed() default false;

    String nextState() default "";

    boolean first() default false;
}

public class StatefulAutonomous extends MecanumOpMode{
    private Long stateStartTime;
    private Long stateEndTime;
    private String nextState;
    private boolean firstLoop = true;
    private Method currentState;

    @Override
    public void init() {
        super.init();

        boolean hasFirst = false;
        for(Method method : this.getClass().getDeclaredMethods()){
            State state = method.getAnnotation(State.class);
            if(state.first()){
                if(hasFirst){
                    throw new RuntimeException("Multiple first states declared");
                }

                currentState = method;
                hasFirst = true;
            }
        }

        if(!hasFirst){
            throw new RuntimeException("Autonomous must have a first state");
        }
    }

    @Override
    public void loop() {
        if(firstLoop){
            firstLoop = false;
            if(currentState.getAnnotation(State.class).timed()){
                State state = currentState.getAnnotation(State.class);
                stateStartTime = System.currentTimeMillis();
                stateEndTime = stateStartTime + (new Double(state.duration()).longValue()*1000);
                nextState = state.nextState();
            }else{
                stateEndTime = null;
                stateStartTime = null;
            }
        }

        if(stateEndTime != null && stateEndTime <= System.currentTimeMillis()){
            if(!nextState.equals("")){
                setNextState(nextState);
            }
        }else{
            try{
                currentState.invoke(this);
            }catch(IllegalAccessException e) {
                e.printStackTrace();
            }catch(InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        for (Component c : components){
            c.doit();
        }
    }

    protected void setNextState(String nextState){
        try {
            currentState = this.getClass().getDeclaredMethod(nextState);
        }catch (NoSuchMethodException e){
            throw new RuntimeException("No next state:"+nextState);
        }
        firstLoop = true;
    }
}
