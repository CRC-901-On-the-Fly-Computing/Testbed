import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatbotMessageComponent } from './chatbot-message.component';

describe('ChatbotMessageComponent', () => {
  let component: ChatbotMessageComponent;
  let fixture: ComponentFixture<ChatbotMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatbotMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatbotMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
