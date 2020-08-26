import { LockRoutingModule } from './lock-routing.module';

describe('LockRoutingModule', () => {
  let lockRoutingModule: LockRoutingModule;

  beforeEach(() => {
    lockRoutingModule = new LockRoutingModule();
  });

  it('should create an instance', () => {
    expect(lockRoutingModule).toBeTruthy();
  });
});
