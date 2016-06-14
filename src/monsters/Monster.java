
package monsters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;

import mechanic.GameElement;
import mechanic.Point;

public class Monster extends GameElement {
	int bounty;
	int lifeCost;
	ArrayList<Point> pathing = new ArrayList<Point>();
	public Monster(int bounty, int lifeCost) {
		super();
		this.setBounty(bounty);
		this.setLifeCost(lifeCost);
	}
	/*
	 * The following pathfinding algorithm is very expensive and bad and is just a placeholder
	 * for testing out pathfinding until we implement a better one
	 */
	
	public ArrayList<Point> placeholderPathFind(double[][] scentGrid, Point loc, Point target) {
		Point currentGridPosition = loc;
		ArrayList<Point> path = new ArrayList<Point>();
		boolean error = false;
		while(!Point.equals(currentGridPosition, target) && path.size() < scentGrid.length * scentGrid[0].length && error == false) { //while the target isn't found and the size isn't too big and there isn't an error in pathfinding
			//System.out.println("iteration is " + path.size());
			//System.out.println("position at " + currentGridPosition.getX() + ", " + currentGridPosition.getY() + " scent at currentpos is " + scentGrid[(int)currentGridPosition.getX()][(int)currentGridPosition.getY()]);
			//System.out.println("target is at " + target.getX() + ", " + target.getY() + " scent at target is " + scentGrid[(int)target.getX()][(int)target.getY()]);
			double greatestScent = 0;
			String dir = "none";
			if(currentGridPosition.getY() > 0) {
				if(scentGrid[(int)currentGridPosition.getX()][(int)currentGridPosition.getY() - 1] > greatestScent) {
					greatestScent = scentGrid[(int)currentGridPosition.getX()][(int)currentGridPosition.getY() - 1];
					dir = "up";
				}
			}
			
			if(currentGridPosition.getY() < scentGrid[0].length - 1) {
				if(scentGrid[(int)currentGridPosition.getX()][(int)currentGridPosition.getY() + 1] > greatestScent) {
					greatestScent = scentGrid[(int)currentGridPosition.getX()][(int)currentGridPosition.getY() + 1];
					dir = "down";
				}
			} 
			
			if(currentGridPosition.getX() > 0) {
				if(scentGrid[(int)currentGridPosition.getX() - 1][(int)currentGridPosition.getY()] > greatestScent) {
					greatestScent = scentGrid[(int)currentGridPosition.getX() - 1][(int)currentGridPosition.getY()];
					dir = "left";
				}
			} 
			
			if(currentGridPosition.getX() < scentGrid.length - 1) {
				if(scentGrid[(int)currentGridPosition.getX() + 1][(int)currentGridPosition.getY()] > greatestScent) {
					greatestScent = scentGrid[(int)currentGridPosition.getX() + 1][(int)currentGridPosition.getY()];
					dir = "right";
				}
			} 
			
			switch (dir){
				case "up":
					path.add(new Point(currentGridPosition.getX(), currentGridPosition.getY() - 1));
					currentGridPosition = new Point(currentGridPosition.getX(), currentGridPosition.getY() - 1);
					break;
				case "down":
					path.add(new Point(currentGridPosition.getX(), currentGridPosition.getY() + 1));
					currentGridPosition = new Point(currentGridPosition.getX(), currentGridPosition.getY() + 1);
					break;
				case "left":
					path.add(new Point(currentGridPosition.getX() - 1, currentGridPosition.getY()));
					currentGridPosition = new Point(currentGridPosition.getX() - 1, currentGridPosition.getY());
					break;
				case "right":
					path.add(new Point(currentGridPosition.getX() + 1, currentGridPosition.getY()));
					currentGridPosition = new Point(currentGridPosition.getX() + 1, currentGridPosition.getY());
					break;
				case "none":
					System.out.println("Error with pathfinding: no detectable scent!");
					error = true;
					break;
			}
		}
		return path;
	}
	//end of bs algorithm
	public void updatePath(Point gDestination) {
		//TODO: replace with more legit pathfinding
		this.pathing.clear();
		this.pathing.addAll(this.placeholderPathFind(this.getMap().getScentGrid(), this.getMap().positionToGrid(this.getLoc()), gDestination));
	}
	public void moveWithPath() {
		if(this.pathing.size() > 0) {
			Point nextPoint = this.pathing.get(0);
			this.moveTowardsPoint(this.getMap().gridToPosition(nextPoint));
			if(this.isReasonableDistanceAwayFrom(this.getMap().gridToPosition(nextPoint))) { //if it's within a reasonable distance from the point, it will consider it to have reached it
				this.changeLoc(this.getMap().gridToPosition(nextPoint));
				this.pathing.remove(0);
			}
		} else { //if it has reached the end of its path
			this.getMap().getUI().changeLives(-this.lifeCost);
			this.setRemove(true);
		}
	}
	public ArrayList<Point> getPath() {
		return this.pathing;
	}
	
	/**
	 * A pathfinding algorithm to find a traversable path from a starting point to a target point
	 * 
	 * @param pathgrid	A grid of occupied (inaccessible) grid squares
	 * @param loc		The starting point
	 * @param target	The target point
	 * @return			An ArrayList<Point> with the first sub-target at the end of list, .get(0) returns the target
	 */
	public ArrayList<Point> pathFind(boolean[][] pathgrid, Point loc, Point target) {
		double[][] efficiency = new double[pathgrid.length][pathgrid[0].length];
		ArrayList<Point> queue = new ArrayList<Point>();
		queue.add(loc);
		efficiency[(int) loc.getX()][(int) loc.getY()] = 0;
		
		while (queue.size() > 0) {
			System.out.println("queue.size = " + queue.size());
			// Sort queue
			queue.sort((Point p1, Point p2) -> {
				return (int) sortByEfficiency(efficiency, p1, p2);
			});
			
			Point base = queue.get(0);
			double baseEffi = efficiency[(int) base.getX()][(int) base.getY()];
			ArrayList<Point> adjacent = Point.proximity8(base);
			for (int index = adjacent.size() - 1; index >= 0; index--) {
				Point test = adjacent.get(index);
				if (test.getX() < 0 || test.getY() < 0) {
					adjacent.set(index, null);
				}
			}
			
			// Find efficiency of closer points and decide if add to queue
			for (int index = 0; index < adjacent.size(); index += 2) {
				Point findEffi = adjacent.get(index);
				if (findEffi != null) {
					double control = efficiency[(int) findEffi.getX()][(int) findEffi.getY()];
					double trial = baseEffi + 1; // Straight path
					if (trial > control) {
						// Find if point is occupied
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()]) { // Assume true in pathgrid means occupied, otherwise, remove negation operator
							queue.add(findEffi);
							efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						}
					}
				}
			}
			for (int index = 1; index < adjacent.size(); index += 2) {
				Point findEffi = adjacent.get(index);
				Point adj1 = adjacent.get(index - 1);
				Point adj2;
				if (index + 1 < adjacent.size()) {
					adj2 = adjacent.get(index + 1);
				} else {
					adj2 = adjacent.get(0);
				}
				if (findEffi != null) {
					double control = efficiency[(int) findEffi.getX()][(int) findEffi.getY()];
					double trial = baseEffi + Math.sqrt(2); // Diagonal path
					if (trial > control) {
						// Assume true in pathgrid means occupied, otherwise, remove negation operator
						if (!pathgrid[(int) findEffi.getX()][(int) findEffi.getY()] && !pathgrid[(int) adj1.getX()][(int) adj1.getY()] 
								&& !pathgrid[(int) adj2.getX()][(int) adj2.getY()]) {
							queue.add(findEffi);
							efficiency[(int) findEffi.getX()][(int) findEffi.getY()] = trial;
						}
					}
				}
			}
			if (Point.equals(base, target)) { // Test if target has been found
				queue.clear(); // While loop will not execute again
			}
		}
		
		double iterateEfficiency = efficiency[(int) target.getX()][(int) target.getY()];
		ArrayList<Point> foundPath = new ArrayList<Point>();
		foundPath.add(target);
		Point current = target;
		while (iterateEfficiency > 0) {
			ArrayList<Point> adjacent = Point.proximity8(current);
			for (int index = adjacent.size(); index > 0; index--) {
				Point test = adjacent.get(index);
				if (test.getX() < 0 || test.getY() < 0) {
					adjacent.set(index, null);
				}
			}
			iterateEfficiency = efficiency[(int) current.getX()][(int) current.getY()];
			double closestEfficiency = Integer.MIN_VALUE;
			int closestIndex = 0;
			for (int index = adjacent.size(); index > 0; index--) {
				if (adjacent.get(index) != null) {
					double localEffi = efficiency[(int) adjacent.get(index).getX()][(int) adjacent.get(index).getY()];
					if (localEffi < iterateEfficiency && localEffi > closestEfficiency) {
						closestEfficiency = localEffi;
						closestIndex = index;
					}
				}
			}
			iterateEfficiency = closestEfficiency;
			current = adjacent.get(closestIndex);
			foundPath.add(current);
		}
		return foundPath;
	}
	
	/**
	 * Determine which of 2 points has better efficiency (lower is better)
	 * 
	 * Parameters must be instantiated
	 * Returns negative if p1 has better efficiency than p2
	 * Returns positive if p1 has worse efficiency than p2
	 * Returns 0 if they have same efficiency (rare case due to use of doubles)
	 * 
	 * @param efficiency	A 2d array of all efficiency scores
	 * @param p1			First point to compare
	 * @param p2			Second point to compare
	 * @return				A value that represents which point has better efficiency
	 */
	private double sortByEfficiency(double[][] efficiency, Point p1, Point p2) {
		double ef1 = efficiency[(int) p1.getX()][(int) p1.getY()];
		double ef2 = efficiency[(int) p1.getX()][(int) p1.getY()];
		return ef1 - ef2;

	}
	@Override
	public void update() {
		this.moveWithPath();
		this.onMonsterUpdate();
	}
	public void onMonsterUpdate() {
		
	}
	@Override
	public void onDeath() {
		this.getMap().getUI().changeMoney(this.bounty);
		this.onMonsterDeath();
	}
	public void onMonsterDeath() {
		
	}
	public void setBounty(int bounty) {
		this.bounty = bounty;
	}
	public int getBounty() {
		return this.bounty;
	}
	public void setLifeCost(int cost) {
		this.lifeCost = cost;
	}
	public int getLifeCost() {
		return this.lifeCost;
	}
}
