package com.project.backend.services.implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.project.backend.domain.entities.QrCode;
import com.project.backend.domain.entities.QrCodeStatusEnum;
import com.project.backend.domain.entities.Ticket;
import com.project.backend.exceptions.QrCodeGenerationException;
import com.project.backend.repository.QrCodeRepo;
import com.project.backend.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepo qrCodeRepo;

    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage( uniqueId );

            QrCode qrCode = new QrCode();
            qrCode.setId( uniqueId );
            qrCode.setStatus( QrCodeStatusEnum.ACTIVE );
            qrCode.setValue( qrCodeImage );
            qrCode.setTicket( ticket );

            return qrCodeRepo.saveAndFlush( qrCode );

        } catch (WriterException | IOException e) {
            throw new QrCodeGenerationException("Failed to generate Qr Code", e);
        }
    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
                );
        BufferedImage qrcodeImage = MatrixToImageWriter.toBufferedImage( bitMatrix );

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write( qrcodeImage, "PNG" , baos );
          byte[] imageBytes = baos.toByteArray();

          return Base64.getEncoder().encodeToString( imageBytes );
        }

    }
}
