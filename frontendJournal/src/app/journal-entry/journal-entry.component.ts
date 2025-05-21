import { Component } from '@angular/core';
import { VerseService } from '../verse.service';
import { Verse } from '../verse.model';

@Component({
  selector: 'app-journal-entry',
  standalone: false,
  templateUrl: './journal-entry.component.html',
  styleUrl: './journal-entry.component.css'
})
export class JournalEntryComponent {
  journalEntry: string = '';
  verses: Verse[] = [];
  loading: boolean = false; 
  noFound: boolean = false;
  
  constructor(private verseService: VerseService) { }
  
  onSubmit(event: Event) {
    event.preventDefault();
    this.loading = true; // Start loading
    //const keywords = this.journalEntry.split(/\s+/).filter(word => word.length > 0);
     // Split and filter out empty strings
    //this.verseService.getVersesByKeywords(keywords).subscribe({
    this.verseService.getVersesByEntryString(this.journalEntry).subscribe({
    next: (verses) => {
      this.verses = verses;
      this.loading = false; // Stop loading
    },
    error: (error) => {
    console.error('Error fetching verses:', error);
      this.verses = []; // Clear previous verses
      this.loading = false; // Stop loading
    }
    });
    if (this.verses = []){
      this.noFound = true;
    }
  }

  clearInput() {
    this.journalEntry = '';
  }
  

  resetNoFound() {
    this.noFound = false;
  }
}
