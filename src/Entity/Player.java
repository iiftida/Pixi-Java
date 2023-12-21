package Entity;
import TileMap.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import GameState.MenuState;

public class Player extends MapObject{

    //player stuff
    private int health;
    private int maxHealth;
    private int score = 0;
    private int maxScore = 43;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    //fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;

    //scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    //gliding
    private boolean gliding;

    //animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {2,8,1,2,4,2,5};

    //animations actions
    private static final int IDLE = 0;
    public static final int WALKING = 1;
    public static final int JUMPING = 2;
    public static final int FALLING = 3;
    public static final int GLIDING = 4;
    public static final int FIREBALL = 5;
    public static final int SCRACHING = 6;

    public Player(TileMap tm)
    {
        super(tm);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        facingRight = true;

        health = maxHealth = 5;
        //score = maxScore = 0;
        fire = maxFire = 2500;

        fireCost = 200;
        fireBallDamage = 5;
        fireBalls = new ArrayList<FireBall>();

        scratchDamage = 8;
        scratchRange = 40;

        //load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Player/playersprites.gif"));

            sprites = new ArrayList<BufferedImage[]>();

            for (int i = 0;i < 7;i++)
            {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for(int j = 0;j < numFrames[i];j++)
                {
                    if(i != SCRACHING)
                    {
                        bi[j] = spritesheet.getSubimage(j * width,i * height,width,height);
                    }
                    else
                    {
                        bi[j] = spritesheet.getSubimage(j * width * 2,i * height,width * 2,height);
                    }

                }
                sprites.add(bi);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);

    }

    public int getHealth()
    {
        return health;
    }

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public int getScore()
    {
        return score;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    public int getFire()
    {
        return fire;
    }

    public int getMaxFire()
    {
        return maxFire;
    }

    public void setFiring()
    {
        firing = true;
    }

    public void setScratching()
    {
        scratching = true;
    }

    public void setGliding(boolean b)
    {
        gliding = b;
    }

    public void setScore(int s)
    {
        score += s;
        if(score>maxScore)
        {
            File file = new File("src/HighScore");
            try
            {
                PrintWriter out = new PrintWriter(file);

                out.println(maxScore);
                setMaxScore();
                out.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setMaxScore()
    {
        int s;
        File file = new File("src/HighScore");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext())
            {
                 maxScore = sc.nextInt();
                //System.out.println();
            }
            sc.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    public void checkAttack(ArrayList<Enemy> enemies)
    {
        //loop through enemies
        for(int i = 0;i < enemies.size();i++)
        {
            Enemy e = enemies.get(i);

            //scratch attack
            if(scratching)
            {
                if(facingRight)
                {
                    if(e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height /2 )
                    {
                        e.hit(scratchDamage);
                    }
                }
                else
                {
                    if(e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < y + height /2 )
                    {
                        e.hit(scratchDamage);
                    }
                }
            }

            //fire attack
            for(int j = 0;j < fireBalls.size();j++)
            {
                if(fireBalls.get(j).intersects(e))
                {
                    e.hit(fireBallDamage);
                    fireBalls.get(j).setHit();
                    break;
                }
            }

            //check enemy collision
            if(intersects(e))
            {
                hit(e.getDamage());
            }

        }

    }

//    public void hit(int damage)
//    {
//        if(flinching)
//        {
//            return;
//        }
//        health -= damage;
//        if(health < 0)
//        {
//            health = 0;
//        }
//        if(health == 0)
//        {
//            dead = true;
//        }
//        flinching = true;
//        flinchTimer = System.nanoTime();
//    }


    public void hit(int damage)
    {
        if(dead || flinching)
        {
            return;
        }
        health -= damage;
        if(health < 0)
        {
            health = 0;
        }
        if(health == 0)
        {
            dead = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime();
    }


    private void getNextPosion()
    {
        //movement
        if(left)
        {
            dx -= moveSpeed;
            if(dx < -maxSpeed)
            {
                dx = -maxSpeed;
            }
        }
        else if(right)
        {
            dx += moveSpeed;
            if(dx > maxSpeed)
            {
                dx = maxSpeed;
            }
        }
        else
        {
            if(dx > 0)
            {
                dx -= stopSpeed;
                if(dx < 0)
                {
                    dx = 0;
                }
            }
            else if(dx < 0)
            {
                dx += stopSpeed;
                if(dx > 0)
                {
                    dx = 0;
                }
            }
        }

        //cannot moving while attacking , except in air
        if((currentAction == SCRACHING || currentAction == FIREBALL) && !(jumping || falling))
        {
            dx = 0;
        }

        //jumping
        if(jumping && !falling)
        {
            dy = jumpStart;
            falling = true;
        }

        //falling
        if(falling)
        {
            if(dy > 0 && gliding)
            {
                dy += fallSpeed * 0.1;
            }
            else
            {
                dy += fallSpeed;
            }

            if(dy > 0)
            {
                jumping = false;
            }
            if(dy < 0 && !jumping)
            {
                dy += stopJumpSpeed;
            }

            if(dy > maxFallSpeed)
            {
                dy = maxFallSpeed;
            }
        }
    }

    public void update()
    {
        //update position
        getNextPosion();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);

        //check attack has stopped
        if(currentAction == SCRACHING)
        {
            if(animation.hasPlayedOnce())
            {
                scratching = false;
            }
        }
        if(currentAction == FIREBALL)
        {
            if(animation.hasPlayedOnce())
            {
                firing = false;
            }
        }

        //fireball attack
        fire += 1;
        if(fire > maxFire)
        {
            fire = maxFire;
        }
        if(firing && currentAction != FIREBALL)
        {
            if(fire > fireCost)
            {
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap,facingRight);
                fb.setPosition(x,y);
                fireBalls.add(fb);
            }
        }

        //update fireballs
        for(int i=0;i < fireBalls.size();i++)
        {
            fireBalls.get(i).update();
            if(fireBalls.get(i).shouldRemove())
            {
                fireBalls.remove(i);
                i--;
            }
        }

        //check done flinching
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000)
            {
                flinching = false;
            }
        }

        //set animation
        if (scratching)
        {
            if (currentAction != SCRACHING)
            {
                currentAction = SCRACHING;
                animation.setFrames(sprites.get(SCRACHING));
                animation.setDelay(50);
                width = 60;

            }
        }
        else if (firing) {
            if (currentAction != FIREBALL) {
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if(dy > 0)
        {
            if(gliding)
            {
                if(currentAction != GLIDING)
                {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            }
            else if(currentAction != FALLING)
            {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if(dy < 0)
        {
            if(currentAction != JUMPING)
            {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }
        else if(left || right)
        {
            if(currentAction != WALKING)
            {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else
        {
            if(currentAction != IDLE)
            {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        //set direction
        if(currentAction != SCRACHING && currentAction != FIREBALL)
        {
            if(right)
            {
                facingRight = true;
            }
            if(left)
            {
                facingRight = false;
            }
        }
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();

        //draw fireball
        for(int i = 0;i < fireBalls.size();i++)
        {
            fireBalls.get(i).draw(g);
        }

        //draw player
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0)
            {
                return;
            }
        }

        super.draw(g);
    }
}










