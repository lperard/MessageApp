package Controller;

public enum Type {
    Hello("HELLO"),
    Connected("CNNTD"),
    Goodbye("GDBYE"),
    ChangePseudo("CHPSD");

	private String type = "";

	Type(String type){
		this.type = type;
	}

	public String toString (){
		return this.type;
	}
}
