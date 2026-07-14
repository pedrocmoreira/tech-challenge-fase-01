package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.application.usecase.BudgetApproveUseCase;
import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@Tag(name = "Orçamento Público", description = "Endpoint acessado pelo cliente via link do e-mail")
public class PublicBudget {
    private final BudgetApproveUseCase budgetApproveUseCase;
    private final ServiceOrderRepository serviceOrderRepository;

    private ServiceOrder filterByNumber(String number){
        return serviceOrderRepository.filterByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", number));
    }

    private String confirmPage(String title, String message, String detail, String color) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>%s</title>
              <style>
                * { margin:0; padding:0; box-sizing:border-box; }
                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                       background:#f4f4f4; display:flex; align-items:center;
                       justify-content:center; min-height:100vh; }
                .card { background:#fff; border-radius:12px; padding:48px 40px;
                        max-width:480px; width:90%%; text-align:center;
                        box-shadow:0 4px 20px rgba(0,0,0,.08); }
                .icon { font-size:56px; margin-bottom:20px; }
                h1 { font-size:24px; color:%s; margin-bottom:12px; }
                p { font-size:15px; color:#555; line-height:1.6; margin-bottom:10px; }
                .badge { display:inline-block; background:#f0f4ff; color:#3B5BDB;
                         border-radius:6px; padding:6px 16px; font-size:13px;
                         font-weight:600; margin-top:20px; }
              </style>
            </head>
            <body>
              <div class="card">
                <div class="icon">%s</div>
                <h1>%s</h1>
                <p>%s</p>
                <p>%s</p>
                <div class="badge">Garage System</div>
              </div>
            </body>
            </html>
            """.formatted(
                title, color,
                title.substring(0, 2),
                title.substring(2).trim(),
                message, detail
        );
    }

    private String errorPage(String number, String detail) {
        return confirmPage(
                "Ação não permitida",
                "Não foi possível processar sua solicitação para a <strong>OS " + number + "</strong>.",
                "Motivo: " + detail + ". Entre em contato com a oficina.",
                "#E67700"
        );
    }

    @GetMapping(value = "/{number}/approve", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Cliente aprova o orçamendo via link do e-mail")
    public String approve(@PathVariable String number){
        ServiceOrder serviceOrder = filterByNumber(number);

        try{
            budgetApproveUseCase.approve(serviceOrder.getId());
            return confirmPage(
                    " Orçamento Aprovado!",
                    "Recebemos sua aprovação para a <strong>OS " + number + "</strong>.",
                    "Nossa equipe já iniciou os trabalhos. Você receberá atualizações em breve.",
                    "#2F9E44"
            );
        } catch (Exception e) {
            return errorPage(number, e.getMessage());
        }
    }

    @GetMapping(value = "/{number}/refuse", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "O Cliente recusa o orçamento via link do e-mail")
    public String refuse(@PathVariable String number){
        ServiceOrder serviceOrder = filterByNumber(number);

        try {
            budgetApproveUseCase.refuse(serviceOrder.getId());
            return confirmPage(
                    " Orçamento Recusado",
                    "Registramos a recusa do orçamento para a <strong>OS " + number + "</strong>.",
                    "A ordem de serviço foi cancelada. Entre em contato conosco se precisar de mais informações.",
                    "#E03131"
            );
        } catch (Exception e) {
            return errorPage(number, e.getMessage());
        }
    }
}
