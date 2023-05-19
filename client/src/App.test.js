import { render, screen } from '@testing-library/react';
import App from './App';

test('renders basic app', () => {
  render(<App />);
  const linkElement = screen.getByText(/Welcome to bugket.com/);
  expect(linkElement).toBeInTheDocument();
});
