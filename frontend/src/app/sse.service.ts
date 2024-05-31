import { Injectable } from '@angular/core';
import { HttpClient ,HttpResponse} from '@angular/common/http';
import { Subject, Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';
import { BackendserviceService } from './backendservice.service';
@Injectable({
  providedIn: 'root'
})
export class SseService {

  private eventSource: EventSource | undefined;
  private sseDataSubject: Subject<string> = new Subject<string>();
  private static retryCount = 0;
  private static readonly MAX_RETRIES = 5;
  //generate unique id for each subscriber
  private static subscriberId = 26;
  constructor(private httpClient: HttpClient,private backendService:BackendserviceService) {
  }

  private connectToSSE() {
    this.eventSource = new EventSource(`http://localhost:8080/teller/subscribe/${this.backendService.decoded.id}` );
    console.log('creating event source');
    this.eventSource.onmessage = event => {
      console.log('received event', event)
      this.sseDataSubject.next(event.data);
    //  this.eventSource!.close();
      this.eventSource = undefined;
    };

    this.eventSource.onerror = error => {
      console.log('error', error);
      if (SseService.retryCount > SseService.MAX_RETRIES) {
        console.log('too many retries');
        this.sseDataSubject.error(error);
        this.eventSource!.close();
        return;
      }
      SseService.retryCount++;
      this.sseDataSubject.error(error);
      this.eventSource!.close();
      this.connectToSSE();
    };

  }
  subscribeToFortuneTeller(): Observable<string> {
    if (!this.eventSource) {
      this.connectToSSE();
    }
    return this.sseDataSubject.asObservable();
  }
  unsubscribeFromFortuneTeller(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }
  }

  requestFortuneTeller(name: string): Observable<HttpResponse<String>> {
    return this.httpClient.get<String>(`http://localhost:8080/teller/future/${name}/${SseService.subscriberId}`,{observe: 'response', headers: { 'Authorization': 'Bearer ' + this.backendService.token }}).pipe(shareReplay());
  }

}
