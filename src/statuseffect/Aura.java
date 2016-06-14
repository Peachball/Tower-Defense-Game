package statuseffect;

import java.util.ArrayList;

import mechanic.GameElement;
import mechanic.Point;
import monsters.Monster;
import towers.Tower;

public class Aura extends StatusEffect {
	float radius;
	TargetType targetType;
	StatusEffect effect;
	static final float SEARCH_INTERVAL = 0.1f;
	ArrayList<GameElement> elementsAffected;
	ArrayList<GameElement> elementsAffectedRemoveBuffer;
	ArrayList<StatusEffect> effectsActive;
	ArrayList<StatusEffect> effectsActiveRemoveBuffer;
	//ArrayList<GameElement> elementsAffectedAddBuffer;
	public Aura(GameElement owner, String auraID, float radius, TargetType targetType, StatusEffect effect) {
		super(owner, StackingProperty.UNSTACKABLE_REPLACE, auraID, -1, 0);
		this.elementsAffected = new ArrayList<GameElement>();
		this.elementsAffectedRemoveBuffer = new ArrayList<GameElement>();
		this.effectsActive = new ArrayList<StatusEffect>();
		this.effectsActiveRemoveBuffer = new ArrayList<StatusEffect>();
		this.interval = SEARCH_INTERVAL;
		this.radius = radius;
		this.effect = effect;
		this.effect.setPermanent(true);
		this.effect.stackingProperty = StackingProperty.UNSTACKABLE_REFRESH_DURATION;
		this.targetType = targetType;
	}
	@Override
	public void onUpdate() {
		if(this.getOwner().getRemove()) { //IF THE OWNER IS GONE, REMOVE THE AURA
			for(StatusEffect se : this.effectsActive) {
				se.setRemove(true);
			}
			this.effectsActive.clear();
		}
	}
	@Override
	public void onInterval() {
		for(StatusEffect se : this.effectsActive) {
			if(Point.getDistance(this.getOwner().getLoc(), se.getOwner().getLoc()) > this.radius) {
				//e.getFirstStatusEffect(this.effect.id, this.effect.getLevel()).changeNumSources(-1);
				se.setRemove(true);
				this.elementsAffectedRemoveBuffer.add(se.getOwner());
				this.effectsActiveRemoveBuffer.add(se);
			}
		}
		for(GameElement e : this.getOwner().getMap().findElementsInArea(this.getOwner().getLoc(), this.radius)) {
			if((targetType == TargetType.BOTH
					|| (targetType == TargetType.MONSTER && e instanceof Monster)
					|| (targetType == TargetType.TOWER && e instanceof Tower)) && !this.elementsAffected.contains(e)) {
				StatusEffect tempEffect = this.effect.clone();
				/*if(e.hasStatusEffect(this.effect.id, this.getLevel())) {
					e.getFirstStatusEffect(this.effect.id, this.getLevel()).changeNumSources(1);
				} else {*/
					e.addStatusEffect(tempEffect);
					tempEffect.setOwner(e);
				//}
				this.elementsAffected.add(e);
				this.effectsActive.add(tempEffect);
			}
		}
		this.elementsAffected.removeAll(elementsAffectedRemoveBuffer);
		this.elementsAffectedRemoveBuffer.clear();
		this.effectsActive.removeAll(effectsActiveRemoveBuffer);
		this.effectsActiveRemoveBuffer.clear();
		//this.elementsAffected.addAll(elementsAffectedAddBuffer);
	}
	public void updateAuraEffect(float radius, StatusEffect effect) {
		//essentially removing the aura and applying a new one
		for(StatusEffect se : this.effectsActive) {
			se.setRemove(true);
		}
		this.elementsAffected.clear();
		this.elementsAffectedRemoveBuffer.clear();
		this.effectsActive.removeAll(effectsActiveRemoveBuffer);
		this.effectsActiveRemoveBuffer.clear();
		
		this.radius = radius;
		this.effect = effect;
		this.effect.setPermanent(true);
		this.onInterval();
	}
	
}
