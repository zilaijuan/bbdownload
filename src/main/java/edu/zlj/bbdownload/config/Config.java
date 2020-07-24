package edu.zlj.bbdownload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: XuyangHwang
 * Date: 2020-07-23
 * Time: 11:23
 */
@Component
public class Config {
    @Value("store.path")
    public String storePath;
}
