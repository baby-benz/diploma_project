package com.itmo.owner.service.cc;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class PemFileService {
    private static final String REMOTE_HOST = "77.235.22.134";
    private static final String REMOTE_ORG1_RELATIVE_PATH = "/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/ca/";
    private static final String REMOTE_ORDERER_RELATIVE_PATH = "/fabric-samples/test-network/organizations/ordererOrganizations/example.com/msp/tlscacerts/";
    private static final String LOCAL_ORG1_RELATIVE_PATH = "org1.example.com/ca/";
    private static final String LOCAL_ORDERER_RELATIVE_PATH = "example.com/ca/";
    private static final String ORG1_FILE_NAME = "ca.org1.example.com-cert.pem";
    private static final String ORDERER_FILE_NAME = "tlsca.example.com-cert.pem";

    @PostConstruct
    private void getCerts() throws Exception {
        getRemotePemFile(REMOTE_ORG1_RELATIVE_PATH, LOCAL_ORG1_RELATIVE_PATH, ORG1_FILE_NAME);
        getRemotePemFile(REMOTE_ORDERER_RELATIVE_PATH, LOCAL_ORDERER_RELATIVE_PATH, ORDERER_FILE_NAME);
    }

    private void getRemotePemFile(String remoteRelativePath, String localRelativePath, String fileName) throws Exception {
        String userName = "hyperuser";
        String password = "*gfhyfc!af820300!gfhujkjdj*";

        FileSystemManager manager = VFS.getManager();

        FileObject local = manager.resolveFile(new File(localRelativePath + fileName).getAbsolutePath());

        FileObject remote = manager.resolveFile("sftp://" + userName + ":" + password + "@" + REMOTE_HOST + remoteRelativePath + fileName);
        local.copyFrom(remote, Selectors.SELECT_SELF);

        local.close();

        remote.close();
    }
}
