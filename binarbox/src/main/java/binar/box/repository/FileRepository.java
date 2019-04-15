package binar.box.repository;

import binar.box.domain.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface FileRepository extends BaseJpaRepository<File, Long> {

    @Query(value = "SELECT f.id FROM file f where f.type=:type", nativeQuery = true)
    List<BigInteger> getFilesIdByType(@Param("type") int type);
//
//    @Query( "SELECT f.path_to_file FROM file f where id in :ids" )
//    List<String> findPathByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM file f where f.type=:type", nativeQuery = true)
    List<File> getFilesPathByType(@Param("type") int type);
}
