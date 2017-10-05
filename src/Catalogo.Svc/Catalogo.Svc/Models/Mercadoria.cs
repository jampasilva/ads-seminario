using System;
using Catalogo.Svc.Models.Base;

namespace Catalogo.Svc.Models
{
    public class Mercadoria : IEntity
    {
        public int ID { get; set; }
        public String Nome { get; set; }
        public double Preco { get; set; } 
    }
}