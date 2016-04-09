/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.horrorho.inflatabledonkey.data.backup;

import java.time.Instant;

/**
 * TimeStatistics.
 *
 * @author Ahseya
 */
public interface TimeStatistics {

    Instant creation();

    Instant modification();

}
