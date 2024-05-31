import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchVacuumComponent } from './search-vacuum.component';

describe('SearchVacuumComponent', () => {
  let component: SearchVacuumComponent;
  let fixture: ComponentFixture<SearchVacuumComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchVacuumComponent]
    });
    fixture = TestBed.createComponent(SearchVacuumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
