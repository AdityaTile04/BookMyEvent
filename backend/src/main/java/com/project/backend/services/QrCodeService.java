package com.project.backend.services;

import com.project.backend.domain.entities.QrCode;
import com.project.backend.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

}
