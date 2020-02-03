public enum Status {
    Local("local"),
    Remote("remote");

	private String status = "";

	Status(String status){
		this.status = status;
	}

	public String toString (){
		return this.status;
	}
}
