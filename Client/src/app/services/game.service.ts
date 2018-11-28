import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import { WebsocketService } from './websocket.service';
import { environment } from '../../environments/environment';

export interface Message {
    author: string,
    message: string
}

@Injectable()
export class GameService {

    public message: Subject<Message>;

    constructor(private wsService: WebsocketService){
        this.message = <Subject<Message>>wsService
            .connect(environment.SERVER_URL)
            .map((response: MessageEvent): Message => {
                let data = JSON.parse(response.data);
                return {
                    author: data.author,
                    message: data.message
                }
            });
    }
}