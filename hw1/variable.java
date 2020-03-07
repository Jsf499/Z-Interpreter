
public class variable {

	String varName = "";
	boolean init = false;
	String value = "";
	String type = "";
	public variable(String var) {
		varName = var;
	}
	public void setVal(String s) {
		value = s;
		init = true;
	}
	public String getVal() {
		return value;
	}
	public void setType(String s) {
		type = s;
	}
	public String getType() {
		return type;
	}
	
	public boolean getInit() {
		return init;
	}
	public String getName() {
		return varName;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
