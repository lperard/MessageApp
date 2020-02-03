public enum Status {
    Local("LOCAL"),
    Remote("REMOTE");

	private String status = "";

	Status(String status){
		this.status = status;
	}

	public String toString (){
		return this.status;
	}
}
