package io.github.cd871127.hodgepodge.cloud.cipher.rsa.mapper;

import io.github.cd871127.hodgepodge.cloud.cipher.crypto.keypair.RsaKeyPair;
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
    @Insert("insert into SECRET_KEY_INFO(KEY_ID, PUBLIC_KEY, PRIVATE_KEY) value(#{keyId},#{publicKey},#{privateKey})")
    int insertRsaKeyPair(RsaKeyPair rsaKeyPair);
}
