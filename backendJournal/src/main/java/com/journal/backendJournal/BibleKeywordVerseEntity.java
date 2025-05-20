package com.journal.backendJournal;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "bible_keyword_verses")
@IdClass(BibleKeywordVerseEntity.BibleKeywordVerseId.class)
public class BibleKeywordVerseEntity {

	@Id
	@Column(name = "keyword", length = 50)
	private String keyword;

	@Lob
	@Column(name = "verse", columnDefinition = "TEXT")
	private String verse;

	@Id
	@Column(name = "place", length = 30)
	private String place;
	
	@Column(name = "link", length = 200)
	private String link;

	public BibleKeywordVerseEntity() {
	}

	public String getKeyword() {
	return keyword;
	}

	public void setKeyword(String keyword) {
	this.keyword = keyword;
	}

	public String getVerse() {
	return verse;
	}

	public void setVerse(String verse) {
	this.verse = verse;
	}

	public String getPlace() {
	return place;
	}

	public void setPlace(String place) {
	this.place = place;
	}
	
	public String getLink() {
	return link;
	}

	public void setLink(String link) {
	this.link = link;
	}
	
	public static class BibleKeywordVerseId implements Serializable {
		private String keyword;
		private String place;

		public BibleKeywordVerseId() {}

		public BibleKeywordVerseId(String keyword, String place) {
		this.keyword = keyword;
		this.place = place;
		}

		public String getKeyword() {
		return keyword;
		}

		public void setKeyword(String keyword) {
		this.keyword = keyword;
		}

		public String getPlace() {
		return place;
		}

		public void setPlace(String place) {
		this.place = place;
		}

		// Override equals() and hashCode() methods
		@Override
		public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BibleKeywordVerseId that = (BibleKeywordVerseId) o;
		return Objects.equals(keyword, that.keyword) && Objects.equals(place, that.place);
		}

		@Override
		public int hashCode() {
		return Objects.hash(keyword, place);
		}
	}
}
