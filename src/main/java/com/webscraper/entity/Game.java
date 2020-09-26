package com.webscraper.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Game {

	@Id
	private String title;
	private float oldPrice;
	private float newPrice;
	private byte discount;
	private String source;

	public Game() {
		super();
	}

	public Game(String title, float oldPrice, float newPrice, byte discount, String source) {
		super();
		this.title = title;
		this.oldPrice = oldPrice;
		this.newPrice = newPrice;
		this.discount = discount;
		this.source = source;
	}

	public Game(String title, String oldPrice, String newPrice, String discount, String source) {
		this.title = title;
		this.oldPrice = Float.parseFloat(oldPrice.replaceAll("[^0-9.,]", ""));
		this.newPrice = Float.parseFloat(newPrice.replaceAll("[^0-9.,]", ""));
		this.discount = Byte.parseByte(discount.replaceAll("[^0-9.,]", ""));
		this.source = source;
	}
	
	public Game(String title,String source) {
		this.title=title;
		this.oldPrice=0;
		this.newPrice=0;
		this.discount=0;
		this.source=source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}

	public float getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(float newPrice) {
		this.newPrice = newPrice;
	}

	public byte getDiscount() {
		return discount;
	}

	public void setDiscount(byte discount) {
		this.discount = discount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + discount;
		result = prime * result + Float.floatToIntBits(newPrice);
		result = prime * result + Float.floatToIntBits(oldPrice);
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (discount != other.discount)
			return false;
		if (Float.floatToIntBits(newPrice) != Float.floatToIntBits(other.newPrice))
			return false;
		if (Float.floatToIntBits(oldPrice) != Float.floatToIntBits(other.oldPrice))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [title=" + title + ", oldPrice=" + oldPrice + ", newPrice=" + newPrice + ", discount=" + discount
				+ ", source=" + source + "]";
	}	

}
