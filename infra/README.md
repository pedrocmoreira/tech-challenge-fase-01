# Infraestrutura — Garage System (Terraform)

Provisiona localmente:
- Cluster Kubernetes via [kind](https://kind.sigs.k8s.io/) (1 control-plane + 1 worker)
- Banco PostgreSQL 16 rodando como Deployment + PVC + Service dentro do cluster
- metrics-server (necessário para o HPA funcionar)

## Pré-requisitos
- [Terraform](https://developer.hashicorp.com/terraform/install) >= 1.9
- [Docker](https://www.docker.com/) rodando
- [kind](https://kind.sigs.k8s.io/docs/user/quick-start/#installation) instalado
- [kubectl](https://kubernetes.io/docs/tasks/tools/#kubectl) instalado

## Como aplicar

\`\`\`bash
cd infra
cp terraform.tfvars.example terraform.tfvars
# edite terraform.tfvars se quiser trocar usuário/senha do banco

terraform init
terraform plan
terraform apply
\`\`\`

## Depois de aplicado

\`\`\`bash
export KUBECONFIG=$(terraform output -raw kubeconfig_path)
kubectl get nodes
kubectl get pods
\`\`\`

## Aplicando os manifestos da aplicação (pasta /k8s)

O Terraform aqui cuida só do **cluster + banco**. A aplicação em si (Deployment, Service, ConfigMap, Secret, HPA) é aplicada separadamente — via `kubectl apply -f k8s/` ou pelo pipeline de CI/CD:

\`\`\`bash
# build da imagem local e carregamento no cluster kind (sem precisar de registry)
docker build -t garage-system:latest ..
kind load docker-image garage-system:latest --name garage-system

kubectl apply -f ../k8s/
\`\`\`

## Destruir tudo

\`\`\`bash
terraform destroy
\`\`\`