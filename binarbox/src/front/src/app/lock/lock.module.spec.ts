import { LockModule } from './lock.module';

describe('LockModule', () => {
  let lockModule: LockModule;

  beforeEach(() => {
    lockModule = new LockModule();
  });

  it('should create an instance', () => {
    expect(lockModule).toBeTruthy();
  });
});
