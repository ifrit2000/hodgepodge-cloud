package io.github.cd871127.hodgepodge.cloud.cipher.mapper;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.CipherAlgorithm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CipherKeyMapper {

    @Select("select key_id from SECRET_KEY_INFO where CIPHER_ALGORITHM=#{cipherAlgorithm}")
    List<String> selectAllKeyId(CipherAlgorithm cipherAlgorithm);
}
