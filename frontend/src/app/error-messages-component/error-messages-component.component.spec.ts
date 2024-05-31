import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorMessagesComponentComponent } from './error-messages-component.component';

describe('ErrorMessagesComponentComponent', () => {
  let component: ErrorMessagesComponentComponent;
  let fixture: ComponentFixture<ErrorMessagesComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ErrorMessagesComponentComponent]
    });
    fixture = TestBed.createComponent(ErrorMessagesComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
