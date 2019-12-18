public enum Type {
    Hello("HELLO"),
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
