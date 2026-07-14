resource "null_resource" "metrics_server" {
  depends_on = [kind_cluster.this]

  provisioner "local-exec" {
    command = <<-EOT
      export KUBECONFIG=${kind_cluster.this.kubeconfig_path}
      kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
      kubectl patch deployment metrics-server -n kube-system --type=json -p='[{"op":"add","path":"/spec/template/spec/containers/0/args/-","value":"--kubelet-insecure-tls"}]'
    EOT
  }
}