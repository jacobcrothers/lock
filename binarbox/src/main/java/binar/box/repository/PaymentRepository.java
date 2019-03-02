package binar.box.repository;

import binar.box.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends BaseJpaRepository<Payment, Long>{

    Payment findByTxnId(String txnId);
}
