export default function Badge({ tone, text }: { tone: "green"|"red"|"orange"|"blue"; text: string }) {
  return <span className={`badge ${tone}`}>{text}</span>;
}
