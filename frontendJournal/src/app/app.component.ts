import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontendJournal';

  toggleMode(){
    document.body.classList.toggle("dark-mode");
  }
}
