import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class VerseService {

private baseUrl = 'https://hongawsdeployment.site';

constructor(private http: HttpClient) { }

getVersesByKeywords(keywords: string[]): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
return this.http.post(`${this.baseUrl}/getVersesByKeywords`, keywords, { headers: headers });
}

getVersesByEntryString(entry: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
return this.http.post(`${this.baseUrl}/getVersesByEntry`, entry, { headers: headers });
}
}