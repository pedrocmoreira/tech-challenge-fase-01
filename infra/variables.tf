variable "cluster_name" {
  description = "Nome do cluster kind"
  type        = string
  default     = "garage-system"
}

variable "kubernetes_version" {
  description = "Versão da imagem de nó do kind"
  type        = string
  default     = "v1.31.0"
}

variable "db_name" {
  description = "Nome do banco de dados"
  type        = string
  default     = "garagesystem_db"
}

variable "db_user" {
  description = "Usuário do banco de dados"
  type        = string
  default     = "garage"
}

variable "db_password" {
  description = "Senha do banco de dados"
  type        = string
  sensitive   = true
  default     = "garage123"
}

variable "db_storage_size" {
  description = "Tamanho do volume persistente do Postgres"
  type        = string
  default     = "2Gi"
}