
package mechanic;

public enum DamageType {
	IMPACT ("impact"),
	PIERCE ("pierce"),
	MAGIC ("magic"),
	ABSOLUTE ("absolute");
	
	String name;
	DamageType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
