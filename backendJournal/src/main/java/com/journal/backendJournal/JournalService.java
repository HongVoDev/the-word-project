package com.journal.backendJournal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class JournalService {
	private BibleKeywordVerseRepository bibleKeywordVerseRepository;
	
	@Autowired
	public void BibleKeywordVerseService(BibleKeywordVerseRepository bibleKeywordVerseRepository) {
	this.bibleKeywordVerseRepository = bibleKeywordVerseRepository;
	}

	public List getVersesByKeyword(List<String> keywords) {
		List<String> verseList = new ArrayList<>();
		for (String keyword : keywords) {
			verseList.addAll(bibleKeywordVerseRepository.findByKeyword(keyword));
		}
		return verseList;
	}
	
	public List getThreeVersesByKeyword(String text) {
		List<BibleKeywordVerseEntity> verseList = new ArrayList<>();
		verseList.addAll(bibleKeywordVerseRepository.findThreeByKeyword(text));
		return verseList;
	}
}
