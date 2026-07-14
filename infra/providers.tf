provider "kind" {}

provider "kubernetes" {
  config_path    = kind_cluster.this.kubeconfig_path
  config_context = "kind-${var.cluster_name}"
}