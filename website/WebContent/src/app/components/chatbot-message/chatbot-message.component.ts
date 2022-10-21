import { Component, OnInit, Input, Directive, AfterViewInit } from '@angular/core';
// FileSelectDirective, FileDropDirective are used in HTML as ng2FileSelect and ng2FileDrop
import { FileSelectDirective, FileDropDirective, FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'app-chatbot-message',
  templateUrl: './chatbot-message.component.html',
  styleUrls: ['./chatbot-message.component.sass'],
})

export class ChatbotMessageComponent implements OnInit, AfterViewInit {
  @Input() public message: any;
  @Input() public URL = '';

  completeAll = false;
  selectedItem: string;
  hasRating = true;

  public uploader: FileUploader = new FileUploader({
    url: this.URL,
    // allowedMimeType: [
    //   'application/zip',
    //   'application/octet-stream',
    //   'application/x-zip-compressed',
    //   'multipart/x-zip',
    // ],
    // allowedFileType: ['application'],
    autoUpload: true,
    // Limit upload files to one
    queueLimit: 1,
  });

  public fileOverBase(e: any): void {
    // Set url that it's come from Input value, default upload url is a empty string;
    // We're doing it here, in case of it didn't change in OnInit
    this.uploader.options.url = this.URL;
  }

  constructor() {
    this.uploader.onCompleteItem = this.onCompleteItem;
    // Determine if the upload proccess is finished
    this.uploader.onCompleteAll = () => {
      this.completeAll = true;
    };
  }

  ngOnInit() {
    // Set url that it's come from Input value, default upload url is a empty string;
    this.uploader.options.url = this.URL;
  }

  ngAfterViewInit() {
    this.uploader.onAfterAddingFile = (item => {
      item.withCredentials = false;
    });
  }

  // When upload  is finished, this method will be called
  onCompleteItem = (item: any, response: any, status: any, headers: any) => {
    // It will call the callback method that is inside the message
    this.message.extraCallback(item, response, status, headers);
  }

  // In case of dropdown, if user choose any of them, this method will be called
  onItemClick = (item: string) => {
    this.message.extraCallback(item);
    return this.selectedItem = item;
  }

  isJson = (str: string) => {
    try {
      JSON.parse(str);
    } catch (e) {
      return false;
    }
    return true;
  }
}
