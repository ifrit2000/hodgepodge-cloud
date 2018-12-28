package io.github.cd871127.hodgepodge.cloud.cipher.mapper;

import io.github.cd871127.hodgepodge.cloud.cipher.algorithm.keypair.RsaKeyPair;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author anthony
 */
@Mapper
public interface RsaMapper {

    /**
     * add rsa key pair
     *
     * @param rsaKeyPair @see RsaKeyPair
     * @return
     */
    @Insert("insert into SECRET_KEY_INFO(KEY_ID, PUBLIC_KEY, PRIVATE_KEY,CIPHER_ALGORITHM) value(#{keyId},#{publicKey},#{privateKey},#{cipherAlgorithm})")
    int insertRsaKeyPair(RsaKeyPair rsaKeyPair);
}
