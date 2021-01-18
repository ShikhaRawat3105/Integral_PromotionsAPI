package resources;

public enum APIResources {

	GetPromotionsAPI("popcorn-api-rs-7.9.10/v1/promotions");
		
	private String resource;

	APIResources(String resource) {
		this.resource = resource;

	}

	public String getResource() {
		return resource;
	}

}