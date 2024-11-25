package com.eucaliptus.springboot_app_billing.service;

import com.eucaliptus.springboot_app_billing.dto.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendBillService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.host}")
    private String username;

    public void sendSale(String to, SaleDTO saleDTO) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();

            model.put("companyName", "Naturista Eucaliptus");
            model.put("companyNIT", "7180744");
            model.put("companyAddress", "Cra. 15 #21 67, Tunja, Boyacá");
            model.put("companyPhone", "3176502842");
            model.put("idBill", saleDTO.getIdSale());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime localDateTime = LocalDateTime.parse(saleDTO.getDateSale().toString(), formatter);
            ZonedDateTime dateUTC = localDateTime.atZone(ZoneId.of("UTC"));
            ZonedDateTime dateBogota = dateUTC.withZoneSameInstant(ZoneId.of("America/Bogota"));
            dateBogota = dateBogota.plusDays(1);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            model.put("dateBill", dateBogota.format(outputFormatter));
            model.put("isClient", true);
            model.put("isProvider", false);
            model.put("clientData", saleDTO.getClientDTO());
            model.put("rows", saleDTO.getSaleDetails().stream().map(SendBillService::saleToRow).toList());
            model.put("invoiceSubtotal", getSubtotal(saleDTO));
            model.put("invoiceTaxes", getTaxes(saleDTO));
            model.put("invoiceTotal", getSubtotal(saleDTO) + getTaxes(saleDTO));

            context.setVariables(model);
            String htmlText = templateEngine.process("sendBill-template", context);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Eucaliptus - Gracias por su compra");
            helper.setText(htmlText, true);

            ClassPathResource imageResource = new ClassPathResource("static/images/logo2.png");
            helper.addInline("logo2.png", imageResource);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPurchase(String to, PurchaseDTO purchaseDTO) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();

            model.put("companyName", "Naturista Eucaliptus");
            model.put("companyNIT", "7180744");
            model.put("companyAddress", "Cra. 15 #21 67, Tunja, Boyacá");
            model.put("companyPhone", "3176502842");
            model.put("idBill", purchaseDTO.getPurchaseId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
            LocalDateTime localDateTime = LocalDateTime.parse(purchaseDTO.getPurchaseDate().toString(), formatter);
            ZonedDateTime dateUTC = localDateTime.atZone(ZoneId.of("UTC"));
            ZonedDateTime dateBogota = dateUTC.withZoneSameInstant(ZoneId.of("America/Bogota"));
            dateBogota = dateBogota.plusDays(1);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            model.put("dateBill", dateBogota.format(outputFormatter));
            model.put("isClient", false);
            model.put("isProvider", true);
            model.put("providerNit", getNit(purchaseDTO.getProviderDTO()));
            model.put("providerName", getName(purchaseDTO.getProviderDTO()));
            model.put("providerPhone", getPhone(purchaseDTO.getProviderDTO()));
            model.put("rows", purchaseDTO.getPurchaseDetails().stream().map(SendBillService::purchaseToRow).toList());
            model.put("invoiceSubtotal", "$"+new DecimalFormat().format(getSubtotal(purchaseDTO)));
            model.put("invoiceTaxes", "$"+new DecimalFormat().format(getTaxes(purchaseDTO)));
            model.put("invoiceTotal", "$"+new DecimalFormat().format(getSubtotal(purchaseDTO) + getTaxes(purchaseDTO)));

            context.setVariables(model);
            String htmlText = templateEngine.process("SendBill-template", context);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Eucaliptus - Gracias por su compra");
            helper.setText(htmlText, true);

            ClassPathResource imageResource = new ClassPathResource("static/images/logo2.png");
            helper.addInline("logo2.png", imageResource);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double getSubtotal(SaleDTO saleDTO) {
        List<SaleDetailDTO> saleDetails = saleDTO.getSaleDetails();
        double total = 0;
        for (SaleDetailDTO saleDetail : saleDetails) {
            total += saleDetail.getSalePriceWithoutIva()*saleDetail.getQuantitySold();
        }
        return total;
    }

    public Double getTaxes(SaleDTO saleDTO){
        List<SaleDetailDTO> saleDetails = saleDTO.getSaleDetails();
        double total = 0;
        for (SaleDetailDTO saleDetail : saleDetails) {
            total += saleDetail.getSalePrice()*saleDetail.getQuantitySold();
        }
        return total - getSubtotal(saleDTO);
    }

    public Double getSubtotal(PurchaseDTO purchaseDTO) {
        List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDTO.getPurchaseDetails();
        double total = 0;
        for (PurchaseDetailDTO purchaseDetailDTO : purchaseDetailDTOS) {
            total += purchaseDetailDTO.getPurchasePriceWithoutIva()* purchaseDetailDTO.getQuantityPurchased();
        }
        return total;
    }

    public Double getTaxes(PurchaseDTO purchaseDTO){
        List<PurchaseDetailDTO> purchaseDetailDTOS = purchaseDTO.getPurchaseDetails();
        double total = 0;
        for (PurchaseDetailDTO purchaseDetailDTO : purchaseDetailDTOS) {
            total += purchaseDetailDTO.getPurchasePrice()*purchaseDetailDTO.getQuantityPurchased();
        }
        return total - getSubtotal(purchaseDTO);
    }

    public String getNit(ProviderDTO providerDTO){
        return (providerDTO.getCompanyDTO() == null) ? providerDTO.getPersonDTO().getIdPerson(): providerDTO.getCompanyDTO().getNit();
    }

    public String getName(ProviderDTO providerDTO){
        return (providerDTO.getCompanyDTO() == null) ? providerDTO.getPersonDTO().getFirstName() + providerDTO.getPersonDTO().getLastName(): providerDTO.getCompanyDTO().getCompanyName();
    }

    public String getPhone(ProviderDTO providerDTO){
        return (providerDTO.getCompanyDTO() == null) ? providerDTO.getPersonDTO().getPhoneNumber(): providerDTO.getCompanyDTO().getCompanyPhoneNumber();
    }

    public static Row saleToRow(SaleDetailDTO saleDetailDTO){
        Row row = new Row();
        row.setProductDTO(saleDetailDTO.getProductDTO());
        row.setQuantity(saleDetailDTO.getQuantitySold());
        row.setUnitPrice("$"+new DecimalFormat().format(saleDetailDTO.getSalePriceWithoutIva()));
        row.setSubtotal("$"+new DecimalFormat().format(saleDetailDTO.getSalePriceWithoutIva()*saleDetailDTO.getQuantitySold()));
        return row;
    }

    public static Row purchaseToRow(PurchaseDetailDTO purchaseDetailDTO){
        Row row = new Row();
        row.setProductDTO(purchaseDetailDTO.getProductDTO());
        row.setQuantity(purchaseDetailDTO.getQuantityPurchased());
        row.setUnitPrice("$"+new DecimalFormat().format(purchaseDetailDTO.getPurchasePriceWithoutIva()));
        row.setSubtotal("$"+new DecimalFormat().format(purchaseDetailDTO.getPurchasePriceWithoutIva()*purchaseDetailDTO.getQuantityPurchased()));
        return row;
    }

}
