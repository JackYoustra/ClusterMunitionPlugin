package com.jackyoustra.clustermunitions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ArrowListener implements Listener {
	@EventHandler
	public void onArrowShoot(ProjectileLaunchEvent event){
		Projectile fired = event.getEntity();
		ProjectileSource shooter = fired.getShooter();
		if(shooter instanceof Player){
			if(fired instanceof Arrow){
				ArrayList<TNTPrimed> tntList = new ArrayList<>(1);
				Arrow targetArrow = (Arrow)fired;
				World arrowWorld = targetArrow.getWorld();
				//FallingBlock fb = arrowWorld.spawnFallingBlock(targetArrow.getLocation(), Material.TNT, (byte) 0x0);
				final TNTPrimed tnt = arrowWorld.spawn(targetArrow.getLocation(), TNTPrimed.class);
				tnt.setVelocity(targetArrow.getVelocity());
				tnt.setFuseTicks(Integer.MAX_VALUE);
				targetArrow.remove();
				tntList.add(tnt);
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, new Runnable() {
					
					@Override
					public void run() {
						for(TNTPrimed currentTNT : tntList){
							if(currentTNT.isOnGround()){
								currentTNT.setFuseTicks(0);
							}
						}
					}
				}, 0, 1); // refresh check time in mills
				Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
					
					@Override
					public void run() {
						if(tnt != null){
							for(int x = -2; x < 2; x++){
								for(int z = -2; z < 2; z++){
									final Location TNTLocation = tnt.getLocation();
									final TNTPrimed newTNT = arrowWorld.spawn(new Location(arrowWorld, TNTLocation.getX()+x, TNTLocation.getY(), TNTLocation.getZ()+z), TNTPrimed.class);
									Vector originalVector = tnt.getVelocity().multiply(new Vector(x*(Math.random()+0.5), 1, z*(Math.random()+0.5)));
									newTNT.setVelocity(originalVector);
									newTNT.setFuseTicks(Integer.MAX_VALUE);
									tntList.add(newTNT);
								}
							}
							tnt.setFuseTicks(0);
						}
						
					}
				}, 30); // cluster explosion after a second, maybe make it so it's distance from the ground?
			}
		}
	}
}
