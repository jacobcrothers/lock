import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SeeAllLocksComponent } from './see-all-locks.component';

describe('SeeAllLocksComponent', () => {
  let component: SeeAllLocksComponent;
  let fixture: ComponentFixture<SeeAllLocksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeeAllLocksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeeAllLocksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
